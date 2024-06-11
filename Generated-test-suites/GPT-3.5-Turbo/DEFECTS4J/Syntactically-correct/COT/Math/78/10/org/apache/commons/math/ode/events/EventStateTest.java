
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
    
    private EventState eventState;
    
    @Before
    public void setUp() {
        eventState = new EventState(null, 1.0, 0.1, 100);
    }
    
    @Test
    public void testGetEventHandler() {
        assertNull(eventState.getEventHandler());
    }
    
    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
    }
    
    @Test
    public void testGetConvergence() {
        assertEquals(0.1, eventState.getConvergence(), 0.0);
    }
    
    @Test
    public void testGetMaxIterationCount() {
        assertEquals(100, eventState.getMaxIterationCount());
    }
    
    @Test
    public void testReinitializeBegin() {
        // TODO: Write test case
        fail("Not implemented");
    }
    
    @Test
    public void testEvaluateStep() {
        // TODO: Write test case
        fail("Not implemented");
    }
    
    @Test
    public void testGetEventTime() {
        // TODO: Write test case
        fail("Not implemented");
    }
    
    @Test
    public void testStepAccepted() {
        // TODO: Write test case
        fail("Not implemented");
    }
    
    @Test
    public void testStop() {
        // TODO: Write test case
        fail("Not implemented");
    }
    
    @Test
    public void testReset() {
        // TODO: Write test case
        fail("Not implemented");
    }
}
