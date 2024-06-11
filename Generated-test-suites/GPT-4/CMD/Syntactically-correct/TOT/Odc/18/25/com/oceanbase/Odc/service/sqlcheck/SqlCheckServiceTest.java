package com.oceanbase.odc.service.sqlcheck;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    ConnectionSession mockSession;

    @Mock
    SqlCheckReq mockReq;

    @Test(expected = NullPointerException.class)
    public void testCheckWithNullSessionAndValidReq() {
        sqlCheckService.check(null, mockReq);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckWithValidSessionAndNullReq() {
        sqlCheckService.check(mockSession, null);
    }

    @Test
    public void testCheckWithValidSessionAndReq() {
        // setup mock rules etc
        Mockito.when(ConnectionSessionUtil.getRuleSetId(mockSession)).thenReturn(1L);
        List<Rule> mockRules = // create mock rules
        Mockito.when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(mockRules);
       
        List<CheckResult> result = sqlCheckService.check(mockSession, mockReq);
        
        // add assertions based on expected result
    }
}