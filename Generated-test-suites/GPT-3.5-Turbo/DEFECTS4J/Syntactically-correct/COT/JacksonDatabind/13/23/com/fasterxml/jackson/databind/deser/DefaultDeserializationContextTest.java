
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.impl.SimpleObjectIdResolver;

public class DefaultDeserializationContextTest {
    
    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(null);
    }
    
    @After
    public void tearDown() {
        deserializationContext = null;
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ReadableObjectId readableObjectId = deserializationContext.findObjectId(id, null, new SimpleObjectIdResolver());
        assertNotNull(readableObjectId);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        try {
            deserializationContext.checkUnresolvedObjectId();
        } catch (Exception e) {
            fail("An exception should not have been thrown");
        }
    }
    
    @Test
    public void testDeserializerInstance() {
        // TODO: Implement test case for deserializerInstance() method
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        // TODO: Implement test case for keyDeserializerInstance() method
    }
    
    @Test
    public void testWith() {
        // TODO: Implement test case for with() method
    }
    
    @Test
    public void testCreateInstance() {
        // TODO: Implement test case for createInstance() method
    }

}
