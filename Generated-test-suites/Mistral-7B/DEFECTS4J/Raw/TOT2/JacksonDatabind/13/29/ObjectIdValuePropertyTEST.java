import org.junit.Assert;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    @Test
    public void testDeserializeAndSet() throws IOException {
        // Arrange
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty();
        ObjectIdReader objectIdReader = createObjectIdReader();
        DeserializationContext context = createDeserializationContext();
        JsonParser jsonParser = createJsonParser();

        Object id = "testId";

        // Act
        objectIdValueProperty.deserializeAndSet(jsonParser, context, null);

        // Assert
        Object expectedId = id;
        Object actualId = context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver).getId();
        Assert.assertEquals(expectedId, actualId);
    }
}