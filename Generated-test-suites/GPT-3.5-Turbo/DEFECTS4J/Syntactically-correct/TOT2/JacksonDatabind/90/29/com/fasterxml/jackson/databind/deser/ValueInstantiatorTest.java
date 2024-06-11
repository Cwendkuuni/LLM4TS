import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.mockito.Mockito;
import java.io.IOException;

public class ValueInstantiatorTest {

    @Test
    public void testGetValueClass() {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Class<?> getValueClass() {
                return String.class;
            }
        };
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public String getValueTypeDesc() {
                return "Test";
            }
        };
        assertEquals("Test", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public boolean canInstantiate() {
                return true;
            }
        };
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateFromString() {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public boolean canCreateFromString() {
                return true;
            }
        };
        assertTrue(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return "default";
            }
        };
        assertEquals("default", valueInstantiator.createUsingDefault(ctxt));
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object delegate = new Object();
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return "delegate";
            }
        };
        assertEquals("delegate", valueInstantiator.createUsingDelegate(ctxt, delegate));
    }

}