package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.*;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
    }

    @Test
    public void testCheckEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.EMPTY_LIST;
        ConnectionConfig config = mock(ConnectionConfig.class);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesConnectionSession() {
        List<Rule> rules = Collections.EMPTY_LIST;
        ConnectionSession session = mock(ConnectionSession.class);

        List<SqlCheckRule> ruleList = sqlCheckService.getRules(rules, session);

        assertNotNull(ruleList);
    }

    @Test
    public void testGetRulesJdbcOperations() {
        List<Rule> rules = Collections.EMPTY_LIST;
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcTemplate.class);

        List<SqlCheckRule> ruleList = sqlCheckService.getRules(rules, dialectType, jdbc);

        assertNotNull(ruleList);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.EMPTY_LIST;
        List<CheckViolation> violations = Collections.EMPTY_LIST;

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(violatedRules);
    }
}
