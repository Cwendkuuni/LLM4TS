
package com.oceanbase.odc.service.sqlcheck;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SqlCheckReq sqlCheckReq;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private Environment environment;
    @Mock
    private ConnectionConfig connectionConfig;

    private SqlCheckService sqlCheckService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
    }
    
    @Test
    public void checkTest() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, SqlCheckReq);
        assertEquals(0, checkResults.size());
    }
    
    @Test
    public void checkEnvironmentTest() {
        when(environmentService.detail(any())).thenReturn(environment);
        List<CheckResult> checkResults = sqlCheckService.checkEnvironment(1L, "database", Collections.emptyList(), connectionConfig);
        assertEquals(0, checkResults.size());
    }
    
    @Test
    public void getRulesTest() {
        List<SqlCheckRule> sqlCheckResults = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertEquals(0, sqlCheckResults.size());
    }
    
    @Test
    public void getRulesDialectTypeTest() {
        List<SqlCheckRule> sqlCheckResults = sqlCheckService.getRules(Collections.emptyList(), DialectType.MYSQL, new JdbcTemplate(dataSource));
        assertEquals(0, sqlCheckResults.size());
    }
    
    @Test
    public void fullFillRiskLevelTest() {
        List<Rule> ruleResults = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), Collections.emptyList());
        assertEquals(0, ruleResults.size());
    }

    
}
