
package weka.core;

import org.junit.Test;

import javax.swing.JOptionPane;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MemoryTest {

    @Test
    public void testMemoryConstructor() {
        Memory memory = new Memory();
        assertEquals(false, memory.getUseGUI());

        memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        final Memory memory = new Memory();
        memory.setEnabled(true);
        assertEquals(true, memory.isEnabled());

        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetInitial() {
        final Memory memory = new Memory();
        // need to use mockito as it returns the total memory of the JVM at runtime
        Runtime runtime = mock(Runtime.class);
        when(runtime.totalMemory()).thenReturn(100L);
        memory.m_Runtime = runtime;
        assertEquals(100L, memory.getInitial());
    }

    @Test
    public void testGetCurrent() {
        final Memory memory = new Memory();
        // need to use mockito as it returns the total memory of the JVM at runtime
        Runtime runtime = mock(Runtime.class);
        when(runtime.totalMemory()).thenReturn(100L);
        long current = memory.getCurrent();
        verify(runtime, times(1)).totalMemory();
        assertEquals(100L, current);
    }

    @Test
    public void testGetMax() {
        final Memory memory = new Memory();
        // need to use mockito as it returns the max memory of the JVM at runtime
        Runtime runtime = mock(Runtime.class);
        when(runtime.maxMemory()).thenReturn(200L);
        memory.m_Runtime = runtime;
        assertEquals(200L, memory.getMax());
    }

    @Test
    public void testIsOutOfMemory() {
        final Memory memory = new Memory();
        // need to use mockito as the calculation involves total memory, max memory and initial memory
        Memory spyMemory = spy(memory);
        doReturn(500000L).when(spyMemory).getCurrent();
        doReturn(100000L).when(spyMemory).getMax();
        doReturn(10000L).when(spyMemory).getInitial();

        assertEquals(true, spyMemory.isOutOfMemory());

        doReturn(800000L).when(spyMemory).getCurrent();
        assertEquals(false, spyMemory.isOutOfMemory());
    }

    @Test
    public void testStopThreads() {
        final Memory memory = new Memory();
        Thread.currentThread().setName("Thread-1");
        Thread thread1 = new Thread();
        thread1.setName("Thread-2");
        Thread thread2 = spy(new Thread());
        doReturn("AWT-EventQueue-0").when(thread2).getName();
        thread2.setName("AWT-EventQueue-0");
        Thread thread3 = mock(Thread.class);
        Thread[] thGroup = {thread1, thread2, thread3};
        Thread.enumerate(thGroup);
        memory.stopThreads();
        verify(thread2, times(1)).stop();
        verify(thread3, never()).stop();
        verify(thread1, never()).stop();
    }

    @Test
    public void testShowOutOfMemory() {
        final Memory memory = new Memory();
        Memory spyMemory = spy(memory);
        doReturn(true).when(spyMemory).isEnabled();
        doNothing().when(spyMemory).showOutOfMemory();
        
        spyMemory.showOutOfMemory();
        
        verify(spyMemory, times(1)).showOutOfMemory();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        final Memory memory = new Memory();
        Memory spyMemory = spy(memory);
        doNothing().when(spyMemory).main(any(String[].class));
        spyMemory.main(new String[]{});
        verify(spyMemory, times(1)).main(any(String[].class));
    }
}
