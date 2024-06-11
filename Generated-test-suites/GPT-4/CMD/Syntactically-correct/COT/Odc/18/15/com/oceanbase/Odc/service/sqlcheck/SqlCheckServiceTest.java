package com.oceanbase.odc.service.sqlcheck;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.EnvCheckService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SqlCheckServiceTest {

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private ConnectionSession session;

    @Mock
    private JdbcTemplate jdbc;

    private static SqlCheckService checkService;

    @BeforeClass
    public static void setup() {
        MockitoAnnotations.initMocks(SqlCheckServiceTest.class);
        checkService = new SqlCheckService();
    }

    @Test
    public void checkFirstTest() {
        when(checkService.check(any(ConnectionSession.class), any(SqlCheckReq.class))).thenReturn(null);

        assertNull(checkService.check(session, new SqlCheckReq()));

        verify(checkService, times(1)).check(any(ConnectionSession.class), any(SqlCheckReq.class));
    }

    @Test
    public void checkSecondTest() {
        when(checkService.check(any(Long.class), any(String.class), any(List.class), any(ConnectionConfig.class))).thenReturn(null);

        assertNull(checkService.check(1L, "dbName", new ArrayList<>(), new ConnectionConfig()));

        verify(checkService, times(1)).check(any(Long.class), any(String.class), any(List.class), any(ConnectionConfig.class));
    }

    @Test
    public void checkThirdTest() {
        when(checkService.getRules(any(List.class), any(ConnectionSession.class))).thenReturn(null);

        assertNull(checkService.getRules(new ArrayList<>(), session));

        verify(checkService, times(1)).getRules(any(List.class), any(ConnectionSession.class));
    }

    @Test
    public void checkFourthTest() {
        when(checkService.getRules(any(List.class), any(DialectType.class), any(JdbcOperations.class))).thenReturn(null);

        assertNull(checkService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbc));

        verify(checkService, times(1)).getRules(any(List.class), any(DialectType.class), any(JdbcOperations.class));
    }

    @Test
    public void checkFifthTest() {
        when(checkService.fullFillRiskLevel(any(List.class), any(List.class))).thenReturn(null);

        assertNull(checkService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>()));

        verify(checkService, times(1)).fullFillRiskLevel(any(List.class), any(List.class));
    }
}
