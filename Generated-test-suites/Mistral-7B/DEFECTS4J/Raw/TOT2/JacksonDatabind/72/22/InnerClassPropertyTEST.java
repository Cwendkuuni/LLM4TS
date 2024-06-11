import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        AnnotatedConstructor constructor = Mockito.mock(AnnotatedConstructor.class);
        when(constructor.getAnnotated()).thenReturn(innerClassProperty._annotated);
        StdDeserializationContext context = Mockito.mock(StdDeserializationContext.class);
        innerClassProperty.deserializeAndSet(jsonParser, context, bean);
        Object value = jsonParser.getValueAsToken().getValueAsString();
        Object result = innerClassProperty.get(bean);
        assertEquals(value, result);
    }
}