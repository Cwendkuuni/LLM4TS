package com.oceanbase.odc.service.sqlcheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;

import java.util.List;
import java.util.Collections;


@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    ConnectionSession session;

    @Mock
    SqlCheckReq req;

    @Before
    public void setUp() throws Exception {
        when(session.getRuleSetId()).thenReturn(null);
    }

    @Test
    public void testCheck() {
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), results);
    }
}