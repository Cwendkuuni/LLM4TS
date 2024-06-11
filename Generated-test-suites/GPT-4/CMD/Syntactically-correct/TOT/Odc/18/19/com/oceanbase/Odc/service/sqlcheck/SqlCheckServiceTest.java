package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.*;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;


@RunWith(SpringRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @MockBean
    private ConnectionSession session;

    @MockBean
    private SqlCheckReq sqlCheckReq;

    @MockBean
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheck() {
        when(sqlCheckReq.getScriptContent()).thenReturn("SELECT * FROM users;");
        sqlCheckService.check(session, sqlCheckReq);  // issue isn't really about capturing return value, it's verifying appropriate interactions
        verify(session, times(1)).getDialectType();  // and so on
    }

    @Test
    public void testCheckWithEnvironmentId() {
        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        sqlCheckService.check(123L, "test_db", Arrays.asList(new OffsetString()), connectionConfig);
        verify(connectionConfig, times(1)).getDialectType();
    }

    @Test
    public void testGetRules() {
        when(session.getDialectType()).thenReturn("MySQL");
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        sqlCheckService.getRules(Arrays.asList(new Rule()), session);
        verify(session, times(1)).getDialectType();
    }
}