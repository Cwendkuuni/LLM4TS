import org.junit.*;
import static org.junit.Assert.*;

public class MemoryTest {
   
   @Test
   public void testIsEnabled() {
      Memory memory = new Memory();
      assertFalse(memory.isEnabled());
   }
   
   @Test
   public void testSetEnabled() {
      Memory memory = new Memory();
      memory.setEnabled(true);
      assertTrue(memory.isEnabled());
   }
   
   @Test
   public void testGetUseGUI() {
      Memory memory = new Memory();
      assertFalse(memory.getUseGUI());
   }
   
   @Test
   public void testGetInitial() {
      Memory memory = new Memory();
      assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
   }
   
   @Test
   public void testGetCurrent() {
      Memory memory = new Memory();
      long currentMemory = memory.getCurrent();
      assertTrue(currentMemory >= 0);
   }
   
   @Test
   public void testGetMax() {
      Memory memory = new Memory();
      long maxMemory = memory.getMax();
      assertTrue(maxMemory > 0);
   }
   
   @Test
   public void testIsOutOfMemory() {
      Memory memory = new Memory();
      assertFalse(memory.isOutOfMemory());
   }
   
   @Test
   public void testToMegaByte() {
      Memory memory = new Memory();
      double result = memory.toMegaByte(1048576);
      assertEquals(1.0, result, 0.01);
   }
   
   @Test
   public void testShowOutOfMemory() {
      Memory memory = new Memory();
      memory.setEnabled(true);
      memory.showOutOfMemory();
   }
   
   @Test
   public void testStopThreads() {
      Memory memory = new Memory();
      memory.stopThreads();
   }
   
   @Test
   public void testGetRevision() {
      Memory memory = new Memory();
      assertNotNull(memory.getRevision());
   }
   
   @Test
   public void testMain() {
      Memory.main(new String[] {});
   }
}