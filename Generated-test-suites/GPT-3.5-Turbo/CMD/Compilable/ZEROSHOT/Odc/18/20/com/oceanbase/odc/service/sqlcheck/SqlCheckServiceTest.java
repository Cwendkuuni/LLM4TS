package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import java.util.List;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {
    @Mock
    private ConnectionSession session;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, null, null);

        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }
}