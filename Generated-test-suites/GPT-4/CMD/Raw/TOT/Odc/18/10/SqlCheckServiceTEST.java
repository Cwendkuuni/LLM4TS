import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService service;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService envService;

    @Test
    public void check_1() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        List<CheckResult> results = service.check(session, req);

        assertTrue(results.isEmpty());
    }

    @Test
    public void getRules_1() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        ConnectionSession session = mock(ConnectionSession.class);

        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> results = service.getRules(rules, session);

        assertFalse(results.isEmpty());
    }
}