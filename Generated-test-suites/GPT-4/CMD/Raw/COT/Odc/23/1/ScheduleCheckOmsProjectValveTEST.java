
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private OscValveContext valveContext;
    
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    
    @Before
    public void setup() {
        assertNotNull(scheduleCheckOmsProjectValve);
    }
    
    @Test
    public void testInvoke() {

        scheduleCheckOmsProjectValve.invoke(valveContext);

    }
}
