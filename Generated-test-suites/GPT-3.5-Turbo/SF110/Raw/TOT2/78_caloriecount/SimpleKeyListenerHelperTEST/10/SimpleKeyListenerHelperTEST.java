import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    
    @Before
    public void setUp() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(new Component());
    }
    
    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(simpleKeyListenerHelper);
    }
    
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }
    
    @Test
    public void testNotifyListener() {
        //TODO: Add test cases for notifyListener method
    }
    
    @Test
    public void testKeyPressed() {
        //TODO: Add test cases for keyPressed method
    }
    
    @Test
    public void testKeyReleased() {
        //TODO: Add test cases for keyReleased method
    }
    
    @Test
    public void testKeyTyped() {
        //TODO: Add test cases for keyTyped method
    }
}