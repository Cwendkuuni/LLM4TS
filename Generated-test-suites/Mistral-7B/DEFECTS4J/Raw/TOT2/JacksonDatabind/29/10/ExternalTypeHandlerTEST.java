import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Given: Mock JsonParser, DeserializationContext, and ExternalTypeHandler
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);

        // When: Call handleTypePropertyValue method with valid input
        String propName = "propertyName";
        Object bean = new Object();
        String typeId = "typeId";
        handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);

        // Then: Verify that the method was called correctly
        verify(mockParser).getText();
        verifyNoMoreInteractions(mockParser);
        verify(mockContext).getBeanDeserializer(bean.getClass());
        verifyNoMoreInteractions(mockContext);

        // Test for edge case: Empty bean
        bean = null;
        handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);
        verify(mockParser, times(2)).getText();
        verify(mockContext).getBeanDeserializer(bean.getClass());
        verifyNoMoreInteractions(mockParser);
        verifyNoMoreInteractions(mockContext);

        // Test for exception handling: Null PropName
        propName = null;
        handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);
        verify(mockParser).getText();
        verify(mockContext).mappingException("Null 'propName'");
        verifyNoMoreInteractions(mockParser);
        verifyNoMoreInteractions(mockContext);
    }

    // Repeat the same test cases for handlePropertyValue method
}