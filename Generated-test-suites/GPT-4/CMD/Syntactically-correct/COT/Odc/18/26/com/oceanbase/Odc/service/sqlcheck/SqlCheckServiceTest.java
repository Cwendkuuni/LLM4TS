package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Collections;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;

public class SqlCheckServiceTest {

    private ConnectionSession mockSession;
    private SqlCheckReq mockReq;
    private SqlCheckService testInstance;

    @Before
    public void setUp() {
        testInstance = new SqlCheckService();
        mockSession = mock(ConnectionSession.class);
        mockReq = mock(SqlCheckReq.class);
    }

    @Test
    public void testCheckMethod() {
        // set behavior
        when(mockSession.getDialectType()).thenReturn(DialectType.MySQL);
        when(mockReq.getDelimiter()).thenReturn(";");
        when(mockReq.getScriptContent()).thenReturn("SELECT * FROM Test;");

        // invoke method
        List<CheckResult> results = testInstance.check(mockSession, mockReq);

        // validate the results
        assertNotNull(results);  // Check that a non-null list is returned
    }

    @Test
    public void testCheckMethodWithEmptyServletContext() {
        // set behavior
        when(mockReq.getScriptContent()).thenReturn("");

        // invoke method
        List<CheckResult> results = testInstance.check(mockSession, mockReq);

        // validate the results
        assertTrue(results.isEmpty());  // should return an empty list if ScriptContent is empty
    }

    @Test(expected = NullPointerException.class)
    public void testCheckMethodWithNullSession() {
        // invoke method
        List<CheckResult> results = testInstance.check(null, mockReq);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckMethodWithNullReq() {
        // invoke method
        List<CheckResult> results = testInstance.check(mockSession, null);
    }

    // Remainder of tests ...

}