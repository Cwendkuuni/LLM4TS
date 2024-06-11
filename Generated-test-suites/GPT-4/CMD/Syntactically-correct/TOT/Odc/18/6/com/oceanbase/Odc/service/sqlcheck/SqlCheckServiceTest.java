package com.oceanbase.odc.service.sqlcheck;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

import java.util.Collections;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckServiceUnderTest;
    
    @Before
    public void setUp() {
        sqlCheckServiceUnderTest = new SqlCheckService();
        ConnectionSessionUtil mockedSessionUtil = Mockito.mock(ConnectionSessionUtil.class);
        Mockito.when(mockedSessionUtil.getRuleSetId(Mockito.any())).thenReturn(null);
        sqlCheckServiceUnderTest.setConnectionSessionUtil(mockedSessionUtil);
    }

    @Test
    public void testCheckSqlWithNoRuleSetIdInSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("SELECT * FROM tbl");
        
        ConnectionSession mockSession = Mockito.mock(ConnectionSession.class);

        List<CheckResult> checkResult = sqlCheckServiceUnderTest.check(mockSession, req);

        assertTrue("Check Result should be empty if ruleset id not in session", checkResult.isEmpty());   
    }

    @Test(expected = NullPointerException.class)
    public void testCheckSqlByEnvironmentWithNullSqls() {
        ConnectionConfig mockConfig = Mockito.mock(ConnectionConfig.class);

        List<CheckViolation> checkViolation = sqlCheckServiceUnderTest.check(1L, "dbname", null, mockConfig);
    }
}