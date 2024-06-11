import org.junit.Test;

public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        boolean enabled = memory.isEnabled();

        //Assert
        assertTrue(enabled);
    }
}