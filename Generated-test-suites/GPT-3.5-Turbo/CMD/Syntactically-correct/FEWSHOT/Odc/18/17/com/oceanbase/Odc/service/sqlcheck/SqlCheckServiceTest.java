package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private SqlCheckReq req;
    private ConnectionConfig config;
    private List<OffsetString> sqls;

    @Before
    public void setup() {
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
        config = new ConnectionConfig();
        sqls = new ArrayList<>();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = Collections.emptyList();
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = Collections.emptyList();
        assertNotNull(sqlCheckService.check(session, req));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";

        Environment env = mock(Environment.class);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        when(factory.getDataSource()).thenReturn(dataSource);

        List<SqlCheckRule> checkRules = Collections.emptyList();
        when(sqlCheckService.getRules(rules, config.getDialectType(), jdbc)).thenReturn(checkRules);

        List<CheckViolation> checkViolations = Collections.emptyList();
        assertNotNull(sqlCheckService.check(environmentId, databaseName, sqls, config));
    }

}