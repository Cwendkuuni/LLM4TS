import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ String.class, JsonParser.class, DeserializationContext.class, JsonDeserializer.class, JsonMappingException.class, Collection.class })
public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("TestString1");
        expectedResult.add("TestString2");
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("TestString1", "TestString2", null);
        when(parser.getCurrentToken()).thenReturn(null, null, null);
        
        Collection<String> result = deserializer.deserialize(parser, context);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserializeWithException() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenThrow(new IOException());
        
        assertThrows(JsonMappingException.class, () -> {
            deserializer.deserialize(parser, context);
        });
    }
    
    @Test
    public void testDeserializeNonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("NonArrayString");
        
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(null);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(null);
        when(parser.getCurrentToken()).thenReturn(null);
        when(context.handleUnexpectedToken(any(), any())).thenReturn(expectedResult);
        
        Collection<String> result = deserializer.deserialize(parser, context);
        
        assertEquals(expectedResult, result);
    }

}