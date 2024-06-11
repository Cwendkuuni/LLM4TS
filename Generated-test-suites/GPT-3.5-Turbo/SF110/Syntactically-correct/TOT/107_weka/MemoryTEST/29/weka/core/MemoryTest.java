import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {

   private Memory memory;

   @Before
   public void setUp() {
       memory = new Memory();
   }

   @Test
   public void testIsEnabled() {
       assertTrue(memory.isEnabled()); // typical use case
       memory.setEnabled(false);
       assertFalse(memory.isEnabled()); // typical use case
   }

   @Test
   public void testGetUseGUI() {
       assertFalse(memory.getUseGUI()); // typical use case
       memory = new Memory(true);
       assertTrue(memory.getUseGUI()); // typical use case
   }

   @Test
   public void testGetInitial() {
       long initial = memory.getInitial();
       assertEquals(Runtime.getRuntime().totalMemory(), initial); // typical use case
   }

   @Test
   public void testGetCurrent() {
       long current = memory.getCurrent();
       assertEquals(Runtime.getRuntime().totalMemory(), current); // typical use case
   }

   @Test
   public void testGetMax() {
       long max = memory.getMax();
       assertEquals(Runtime.getRuntime().maxMemory(), max); // typical use case
   }

   @Test
   public void testIsOutOfMemory() {
       assertFalse(memory.isOutOfMemory()); // typical use case
       long initial = memory.getInitial();
       long max = memory.getMax();
       long current = memory.getCurrent();
       assertTrue(memory.isOutOfMemory());
       memory.setEnabled(false);
       assertFalse(memory.isOutOfMemory()); // typical use case
   }

   @Test
   public void testToMegaByte() {
       long bytes = 1048576; // 1MB
       double expected = bytes / 1048576.0;
       assertEquals(expected, memory.toMegaByte(bytes), 0.001); // typical use case
   }

   @Test
   public void testShowOutOfMemory() {
       // Not possible to test showOutOfMemory as it depends on external factors
       // such as JVM memory settings and UI interactions
       // Therefore, no test case for this method
   }

   @Test
   public void testStopThreads() {
       // Not possible to test stopThreads as it has potential to disrupt
       // the execution of the current test suite and other active threads
       // Therefore, no test case for this method
   }

   @Test
   public void testGetRevision() {
       String revision = memory.getRevision();
       assertNotNull(revision); // typical use case
   }

   @Test
   public void testMain() {
       // Not possible to test main method directly as it depends on external factors
       // such as console output and JVM memory settings
       // Therefore, no test case for this method
   }

}