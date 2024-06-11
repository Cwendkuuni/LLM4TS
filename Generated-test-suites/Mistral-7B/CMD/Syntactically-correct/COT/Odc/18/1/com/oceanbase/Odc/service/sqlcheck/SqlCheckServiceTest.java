package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.model.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

import lombok.NonNull;

@Component
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    private static final String TEST_ENV_ID = "test_env_id";
    private static final String TEST_DATABASE_NAME = "test_database_name";
    private static final String TEST_RULESET_ID = "test_ruleset_id";
    private static final String TEST_CONNECTION_CONFIG = "test_connection_config";
    private static final String TEST_SCRIPT_CONTENT = "test_script_content";
    private static final String TEST_SCRIPT = "test_script";

    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private SqlCheckService sqlCheckService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @org.junit.Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent(TEST_SCRIPT_CONTENT);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());
    }

    @org.junit.Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        Long ruleSetId = TEST_RULESET_ID;
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
        List<CheckViolation> checkViolations = sqlCheckRules.get(0).check(Collections.singletonList(TEST_SCRIPT));
        assertTrue(checkViolations.isEmpty());
    }

    @org.junit.Test
    public void testCheckWithViolations() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        Long ruleSetId = TEST_RULESET_ID;
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("TestRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        RuleViolation violation1 = new RuleViolation();
        violation1.setMessage("Test violation 1");
        violation1.setLevel("LOW");
        rule1.setViolation(violation1);
        rules.add(rule1);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("TestRule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        RuleViolation violation2 = new RuleViolation();
        violation2.setMessage("Test violation 2");
        violation2.setLevel("MEDIUM");
        rule2.setViolation(violation2);
        rules.add(rule2);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
        List<CheckViolation> checkViolations = sqlCheckRules.get(0).check(Collections.singletonList(TEST_SCRIPT));
        assertFalse(checkViolations.isEmpty());
        assertEquals(2, checkViolations.size());
        assertEquals("Test violation 1", checkViolations.get(0).getMessage());
        assertEquals("Test violation 2", checkViolations.get(1).getMessage());
    }

    @org.junit.Test
    public void testCheckWithEnvironment() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        Environment env = new Environment();
        env.setId(TEST_ENV_ID);
        env.setRulesetId(TEST_RULESET_ID);
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(TEST_SCRIPT));
        ConnectionConfig config = new ConnectionConfig();
        config.setId(TEST_CONNECTION_CONFIG);
        config.setDialectType(DialectType.MYSQL);
        List<CheckViolation> checkViolations = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, config);
        assertTrue(checkViolations.isEmpty());
    }

    @org.junit.Test
    public void testCheckWithViolationsInEnvironment() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        Environment env = new Environment();
        env.setId(TEST_ENV_ID);
        env.setRulesetId(TEST_RULESET_ID);
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("TestRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        RuleViolation violation1 = new RuleViolation();
        violation1.setMessage("Test violation 1");
        violation1.setLevel("LOW");
        rule1.setViolation(violation1);
        rules.add(rule1);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("TestRule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        RuleViolation violation2 = new RuleViolation();
        violation2.setMessage("Test violation 2");
        violation2.setLevel("MEDIUM");
        rule2.setViolation(violation2);
        rules.add(rule2);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertFalse(sqlCheckRules.isEmpty());
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(TEST_SCRIPT));
        List<CheckViolation> checkViolations = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, config).get(0);
        assertFalse(checkViolations.isEmpty());
        assertEquals(2, checkViolations.size());
        assertEquals("Test violation 1", checkViolations.get(0).getMessage());
        assertEquals("Test violation 2", checkViolations.get(1).getMessage());
    }
}

@Component
class DefaultSqlCheckerTest {

    // Add test cases for other methods (check, getRules, fullFillRiskLevel) here

}