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
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Slf4j
public class SqlCheckServiceTest {

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private JdbcTemplate jdbcTemplate;
    private ConnectionSession session;
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService() {{
            this.ruleService = ruleService;
            this.environmentService = environmentService;
        }};
        jdbcTemplate = new JdbcTemplate(new BasicDataSource());
        session = Mockito.mock(ConnectionSession.class);
        singleConnectionDataSource = Mockito.mock(SingleConnectionDataSource.class);
    }

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        Long ruleSetId = 1L;
        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(2, sqlCheckRules.size());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM employees WHERE age > 30");
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(2, checkResults.size());

        verify(ruleService, times(1)).list(ruleSetId, ArgumentMatchers.any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsAndConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<Rule> rules = createRules();
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM employees WHERE age > 30"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Environment env = new Environment();
        env.setRulesetId(ruleSetId);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());
        assertEquals(0, violatedRules.size());

        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);
        assertEquals(2, checkRules.size());

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(2, checkViolations.size());

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), ArgumentMatchers.any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
    }

    private List<Rule> createRules() {
        Rule rule1 = new Rule();
        RuleMetadata rule1Metadata = new RuleMetadata();
        rule1Metadata.setName("Rule1");
        rule1Metadata.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(rule1Metadata);
        rule1.setEnabled(true);

        Rule rule2 = new Rule();
        RuleMetadata rule2Metadata = new RuleMetadata();
        rule2Metadata.setName("Rule2");
        rule2Metadata.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(rule2Metadata);
        rule2.setEnabled(false);

        Rule rule3 = new Rule();
        RuleMetadata rule3Metadata = new RuleMetadata();
        rule3Metadata.setName("Rule3");
        rule3Metadata.setType(RuleType.SQL_CHECK);
        rule3.setMetadata(rule3Metadata);
        rule3.setEnabled(true);

        return new ArrayList<>(List.of(rule1, rule2, rule3));
    }
}