import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {
    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @After
    public void tearDown() {
        memory = null;
    }

    @Test
    public void testIsEnabled() {
        Assert.assertTrue(memory.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        memory.setEnabled(false);
        Assert.assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Assert.assertFalse(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        long initial = memory.getInitial();
        Assert.assertEquals(Memory.m_Initial, initial);
    }

    @Test
    public void testGetCurrent() {
        long current = memory.getCurrent();
        Assert.assertEquals(memory.m_Runtime.totalMemory(), current);
    }

    @Test
    public void testGetMax() {
        long max = memory.getMax();
        Assert.assertEquals(memory.m_Max, max);
    }

    @Test
    public void testIsOutOfMemory() {
        memory.setEnabled(true);
        memory.setMax(memory.getCurrent() + 200000L);
        Assert.assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 1048576L;
        double megabytes = Memory.toMegaByte(bytes);
        Assert.assertEquals(1.0, megabytes, 0.001);
    }

    @Test
    public void testShowOutOfMemory() {
        // Not possible to test console output
    }

    @Test
    public void testStopThreads() {
        // Not possible to test threads stopping
    }

    @Test
    public void testGetRevision() {
        String revision = memory.getRevision();
        Assert.assertEquals(RevisionUtils.extract("$Revision: 8034 $"), revision);
    }
}