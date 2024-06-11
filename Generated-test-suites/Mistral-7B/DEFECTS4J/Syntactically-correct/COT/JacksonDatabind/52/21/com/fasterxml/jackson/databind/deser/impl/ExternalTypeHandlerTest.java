import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private Object bean;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{\"typeId1\": \"Type1\", \"typeId2\": null}");
        bean = new Object();
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
    }

    @After
    public void tearDown() throws IOException {
        parser.close();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, null, "typeId1", bean);
        assertNotNull(handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValueWithNullTypeId() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, null, "typeId2", bean);
        assertNull(handler._typeIds[1]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        handler.start();
        handler.handlePropertyValue(parser, null, "property1", bean);
        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithMissingProperty() throws IOException {
        handler.start();
        handler.handlePropertyValue(parser, null, "missingProperty", bean);
    }

    @Test
    public void testComplete() throws IOException {
        handler.start();
        handler.handlePropertyValue(parser, null, "property1", bean);
        handler.handleTypePropertyValue(parser, null, "typeId1", bean);
        handler.handlePropertyValue(parser, null, "property2", bean);
        handler.handleTypePropertyValue(parser, null, "typeId2", bean);
        Object result = handler.complete(parser, null, bean);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingTypeId() throws IOException {
        handler.start();
        handler.handlePropertyValue(parser, null, "property1", bean);
        handler.handlePropertyValue(parser, null, "property2", bean);
        handler.complete(parser, null, bean);
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        handler.start();
        handler.handleTypePropertyValue(parser, null, "typeId1", bean);
        handler.handleTypePropertyValue(parser, null, "typeId2", bean);
        handler.complete(parser, null, bean);
    }
}