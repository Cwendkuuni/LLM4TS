import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ValueInstantiatorTest {

    @Test
    public void testCanInstantiate() {
      ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public boolean canCreateUsingDefault() {
                return false;
            }

            @Override
            public boolean canCreateFromObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateFromString() {
                return false;
            }

            @Override
            public boolean canCreateFromInt() {
                return false;
            }

            @Override
            public boolean canCreateFromLong() {
                return false;
            }

            @Override
            public boolean canCreateFromDouble() {
                return false;
            }

            @Override
            public boolean canCreateFromBoolean() {
                return false;
            }

            @Override
            public boolean canInstantiate() {
                return super.canInstantiate();
            }
        };

        boolean result = valueInstantiator.canInstantiate();
        Assert.assertFalse(result);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        DeserializationContext ctxt = null;
        Object result = valueInstantiator.createUsingDefault(ctxt);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testGetValueTypeDesc() {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public String getValueTypeDesc() {
                return "TestValue";
            }
        };

        String result = valueInstantiator.getValueTypeDesc();
        Assert.assertEquals("TestValue", result);
    }
}