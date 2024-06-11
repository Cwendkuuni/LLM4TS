
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.*;
import com.oceanbase.odc.service.sqlcheck.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SqlCheckReq sqlCheckReq;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private JdbcOperations jdbc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void check_connSession_sqlCheckReq_test() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.ANSI);
        when(sqlCheckReq.getDelimiter()).thenReturn(";");   
        when(sqlCheckReq.getScriptContent()).thenReturn("Test Script Content"); 
        assertNotNull(sqlCheckService.check(connectionSession, sqlCheckReq));
    }

    @Test
    public void check_with_environmentId_databasename_sqls_config_test(){
        Environment env = mock(Environment.class);
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.ANSI);
        when(environmentService.detail(anyLong())).thenReturn(env);
        List<OffsetString> offsetStrings = Arrays.asList(mock(OffsetString.class));
        assertNotNull(sqlCheckService.check(1L, "testDB", offsetStrings, connectionConfig));
    }

    @Test
    public void getRules_test() {
        List<Rule> rules = Arrays.asList(new Rule());
        when(connectionSession.getDialectType()).thenReturn(DialectType.ANSI);
        assertNotNull(sqlCheckService.getRules(rules, connectionSession));
    }

    @Test
    public void fullFillRiskLevel_test() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation());
        assertNotNull(sqlCheckService.fullFillRiskLevel(rules, checkViolations));
    }

}
