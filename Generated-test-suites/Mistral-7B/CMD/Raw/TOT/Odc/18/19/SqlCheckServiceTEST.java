package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

public class SqlCheckServiceTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn( Arrays.asList(
                mockRule("Rule1", RuleType.SQL_CHECK, Level.LOW),
                mockRule("Rule2", RuleType.SQL_CHECK, Level.MEDIUM),
                mockRule("Rule3", RuleType.SQL_CHECK, Level.HIGH)
        ));
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
    }

    @Test
    public void testCheck_withValidConnectionSessionAndValidScriptContent() {
        // Given
        List<Rule> rules = new ArrayList<>();
        List<CheckResult> expectedCheckResults = Arrays.asList(
                new CheckResult(new CheckViolation("Rule1", "Violation1"), Level.LOW),
                new CheckResult(new CheckViolation("Rule2", "Violation2"), Level.MEDIUM)
        );
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM users WHERE age > 18");

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertEquals(expectedCheckResults, checkResults);
        verify(ruleService, times(1)).list(eq(1L), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheck_withEmptyScriptContent() {
        // Given
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq("");

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertTrue(checkResults.isEmpty());
        verifyNoInteractions(ruleService);
    }

    @Test
    public void testCheck_withInvalidConnectionSession() {
        // Given
        when(session.getRuleSetId()).thenReturn(null);

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("SELECT * FROM users"));

        // Then
        assertTrue(checkResults.isEmpty());
        verifyNoInteractions(ruleService);
    }

    @Test
    public void testCheck_withEmptyRules() {
        // Given
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM users");

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertTrue(checkResults.isEmpty());
        verifyNoInteractions(ruleService);
    }

    @Test
    public void testCheck_withEnvironmentIdAndDatabaseName() {
        // Given
        List<Rule> rules = Arrays.asList(
                mockRule("Rule1", RuleType.SQL_CHECK, Level.LOW),
                mockRule("Rule2", RuleType.SQL_CHECK, Level.MEDIUM),
                mockRule("Rule3", RuleType.SQL_CHECK, Level.HIGH)
        );
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(factory.getDataSource()).thenReturn(dataSource);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        when(sqlCheckService.getRules(anyList(), eq(DialectType.MySQL), eq(jdbc))).thenReturn(rules);
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users"));
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> expectedCheckViolations = Arrays.asList(
                new CheckViolation("Rule1", "Violation1"),
                new CheckViolation("Rule2", "Violation2")
        );

        // When
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDB", sqls, config);

        // Then
        assertEquals(expectedCheckViolations, checkViolations);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
        verify(sqlCheckService, times(1)).getRules(anyList(), eq(DialectType.MySQL), eq(jdbc));
    }

    private Rule mockRule(String name, RuleType type, Level level) {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(name);
        metadata.setType(type);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rule.setLevel(level);
        return rule;
    }

    private Level mockLevel(int level) {
        return new Level(level);
    }

    private static class Level {
        private final int level;

        Level(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
}