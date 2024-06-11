import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EventStateTest {

 @BeforeEach
 public void setup() {
 handler = mock(EventHandler.class);
 eventState = new EventState(handler, 0.01, 0.001, 10);
 }

 @Test
 public void testInitialValues() {
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 assertNull(eventState.getEventHandler());
 assertNull(eventState.getPreviousEventTime());
 assertNull(eventState.getT0());
 assertNull(eventState.getG0());
 assertFalse(eventState.isForward());
 assertFalse(eventState.isIncreasing());
 assertFalse(eventState.isPendingEvent());
 }

 @Test
 public void testReinitializeBegin() throws EventException {
 double tStart = 1.0;
 double[] yStart = new double[]{1.0, 2.0};
 eventState.reinitializeBegin(tStart, yStart);
 assertEquals(tStart, eventState.getT0());
 assertEquals(handler, eventState.getEventHandler());
 assertEquals(0.0, eventState.getG0(), 1e-6);
 assertTrue(eventState.isG0Positive());
 }

 @Test
 public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
 StepInterpolator interpolator = mock(StepInterpolator.class);
 when(interpolator.isForward()).thenReturn(true);
 when(interpolator.getCurrentTime()).thenReturn(1.5);
 double h = 0.1;
 when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.1, 2.1});
 UnivariateRealFunction f = mock(UnivariateRealFunction.class);
 when(handler.g(anyDouble(), anyArray())).thenReturn(1.0, -1.0, 0.0);
 when(handler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.RESET_DERIVATIVES);
 BrentSolver solver = new BrentSolver();
 solver.setAbsoluteAccuracy(0.001);
 solver.setMaximalIterationCount(10);
 when(f.getValue(anyDouble())).thenAnswer(i -> {
 double value = handler.g((double) i.getArguments()[0], (double[]) i.getArguments()[1]);
 return value;
 });

 eventState.evaluateStep(interpolator);
 assertEquals(1.1666666666666666, eventState.getT0(), 1e-6);
 assertEquals(-1.0, eventState.getG0(), 1e-6);
 assertTrue(eventState.isG0Positive());
 assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
 }

 @Test
 public void testStepAccepted() throws EventException {
 double t = 2.0;
 double[] y = new double[]{3.0, 4.0};
 eventState.stepAccepted(t, y);
 assertEquals(t, eventState.getT0());
 assertEquals(-1.0, eventState.getG0(), 1e-6);
 assertFalse(eventState.isG0Positive());
 assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
 }

 @Test
 public void testStop() {
 eventState.setNextAction(EventHandler.STOP);
 assertTrue(eventState.stop());
 }

 @Test
 public void testReset() throws EventException {
 double t = 3.0;
 double[] y = new double[]{5.0, 6.0};
 eventState.reset(t, y);
 assertFalse(eventState.isPendingEvent());
 }

}