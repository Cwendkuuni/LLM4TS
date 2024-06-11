package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.factory.ListFactory;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Component
public class SqlCheckServiceTest {

    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private SqlCheckReq req;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
        rules = Collections.singletonList(mock(Rule.class));
        sqlCheckRules = Collections.singletonList(mock(SqlCheckRule.class));
        checkViolations = new ArrayList<>();
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, eq(session.getDialectType()), any(JdbcOperations.class)))
                .thenReturn(sqlCheckRules);
        SqlChecker sqlChecker = new DefaultSqlChecker(session.getDialectType(), req.getDelimiter(),
                sqlCheckRules);
        when(sqlChecker.check(anyList())).thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(1, result.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, eq(session.getDialectType()), any(JdbcOperations.class));
        verify(sqlChecker, times(1)).check(anyList());
        verifyNoMoreInteractions(sqlChecker);
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testCheckEnvironment() {
        Environment env = mock(Environment.class);
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(ruleService.list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(mock(SingleConnectionDataSource.class));
        JdbcTemplate jdbc = new JdbcTemplate(mock(JdbcOperations.class));
        when(sqlCheckService.getRules(rules, any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(sqlCheckRules);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(any(DialectType.class), null, sqlCheckRules);
        List<OffsetString> sqls = ListFactory.list(new OffsetString("test sql"));
        when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(anyLong(), "testDatabaseName", sqls, mock(ConnectionConfig.class));
        assertEquals(1, result.size());
        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class));
        verify(factory, times(1)).getDataSource();
        verify(sqlCheckService, times(1)).getRules(rules, any(DialectType.class), any(JdbcOperations.class));
        verify(sqlChecker, times(1)).check(anyList(), any(SqlCheckContext.class));
        verifyNoMoreInteractions(sqlChecker);
        verifyNoMoreInteractions(factory);
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(environmentService);
    }

    @Test
    public void testGetRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        List<Rule> ruleList = Collections.singletonList(mock(Rule.class));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(ruleList);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(1, result.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(session);
    }

    @Test
    public void testGetRulesWithJdbc() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(sqlCheckService.getRules(rules, dialectType, jdbc)).thenReturn(sqlCheckRules);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertEquals(1, result.size());
        verify(sqlCheckService, times(1)).getRules(rules, dialectType, jdbc);
        verifyNoMoreInteractions(sqlCheckService);
        verifyNoMoreInteractions(rules);
        verifyNoMoreInteractions(jdbc);
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule = mock(Rule.class);
        CheckViolation violation = mock(CheckViolation.class);
        String name = "${SqlCheckRuleType}";
        RuleMetadata metadata = mock(RuleMetadata.class);
        when(rule.getMetadata()).thenReturn(metadata);
        when(metadata.getName()).thenReturn(name);
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getEnabled()).thenReturn(true);
        CheckResult checkResult = new CheckResult();
        checkResult.setViolations(Collections.singletonList(violation));

        List<CheckResult> checkResults = new ArrayList<>();
        checkResults.add(checkResult);

        List<Rule> results = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(1, results.size());
        verify(rule, times(1)).getMetadata();
        verify(metadata, times(1)).getName();
        verify(metadata, times(1)).getType();
        verify(rule, times(1)).getEnabled();
        verifyNoMoreInteractions(rule);
        verifyNoMoreInteractions(metadata);
        verifyNoMoreInteractions(checkResult);
        verifyNoMoreInteractions(checkViolations);
    }
}