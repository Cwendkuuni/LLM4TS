
import static org.junit.Assert.*;
import org.junit.Test;

public class ClassInfoTest {
    
    @Test
    public void testIsAbstract() {
        //Test when class is not abstract
        assertFalse(new ClassInfo().isAbstract());
        
        //Add more test cases for edge cases or exceptions if needed
    }
    
    @Test
    public void testIsInterface() {
        //Test when class is not an interface
        assertFalse(new ClassInfo().isInterface());
        
        //Add more test cases for edge cases or exceptions if needed
    }
    
    @Test
    public void testGetClassName() {
        //Test the correctness of class name
        assertEquals("javax.swing.WindowConstants", new ClassInfo().getClassName());
        
        //Add more test cases for edge cases or exceptions if needed
    }
    
    //Add more test methods for other public methods in ClassInfo class as per the instructions
}
