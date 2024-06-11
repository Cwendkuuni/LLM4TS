import static org.junit.Assert.assertDoesNotThrow;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType validType = mock(JavaType.class);
        new SubTypeValidator().validateSubType(context, validType);
         assertDoesNotThrow(() -> new SubTypeValidator().validateSubType(context, validType));
    }

    @Test
    public void testValidateSubType_IllegalType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType illegalType = mock(JavaType.class);
        when(illegalType.getRawClass().getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

        assertThrows(JsonMappingException.class, () -> new SubTypeValidator().validateSubType(context, illegalType));
    }
}