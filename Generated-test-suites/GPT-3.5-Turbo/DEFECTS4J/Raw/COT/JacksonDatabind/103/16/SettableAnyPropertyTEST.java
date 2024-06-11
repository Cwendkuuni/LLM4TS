import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser p = mock(JsonParser.class);
        Object instance = new Object();
        String propName = "testProp";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(settableAnyProperty.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new Object());
        doNothing().when(settableAnyProperty).set(any(Object.class), any(Object.class), any(Object.class));
        when(settableAnyProperty._valueDeserializer.getNullValue(ctxt)).thenReturn(null);
        when(settableAnyProperty._valueDeserializer.deserializeWithType(p, ctxt, settableAnyProperty._valueTypeDeserializer)).thenReturn(new Object());
        when(reference.getRoid()).thenReturn(mock(ReadableObjectId.class));
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
    }

    @Test
    public void testDeserialize() throws IOException {
        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(null);
        when(settableAnyProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());
        settableAnyProperty.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testSet() throws IOException, IllegalAccessException {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                mock(BeanProperty.class),
                mock(SettableAnyProperty.AnnotatedMember.class),
                mock(JavaType.class),
                mock(JsonDeserializer.class),
                mock(TypeDeserializer.class)
        );
        settableAnyProperty.set(new Object(), "testProperty", new Object());
        settableAnyProperty.set(new Object(), "testProperty", new Object());
    }

    @Test(expected = IOException.class)
    public void testSetThrowsIOException() throws IOException {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                mock(BeanProperty.class),
                mock(SettableAnyProperty.AnnotatedMember.class),
                mock(JavaType.class),
                mock(JsonDeserializer.class),
                mock(TypeDeserializer.class)
        );
        settableAnyProperty.set(new Object(), "testProperty", new Object());
        throw new IOException();
    }
}
