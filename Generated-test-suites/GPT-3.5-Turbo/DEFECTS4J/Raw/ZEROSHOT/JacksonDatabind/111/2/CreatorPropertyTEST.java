import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorProperty;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() {
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    }

    @Test
    public void testGetName() {
        Assert.assertNull(creatorProperty.getName());
    }

    @Test
    public void testGetType() {
        Assert.assertNull(creatorProperty.getType());
    }

    @Test
    public void testGetWrapperName() {
        Assert.assertNull(creatorProperty.getWrapperName());
    }

    @Test
    public void testGetTypeDeserializer() {
        Assert.assertNull(creatorProperty.getTypeDeserializer());
    }

    @Test
    public void testGetContextAnnotations() {
        Assert.assertNull(creatorProperty.getContextAnnotations());
    }

    @Test
    public void testGetAnnotated() {
        Assert.assertNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        Assert.assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        creatorProperty.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        creatorProperty.deserializeSetAndReturn(null, null, null);
    }

    @Test
    public void testSet() throws IOException {
        creatorProperty.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        creatorProperty.setAndReturn(null, null);
    }

    @Test
    public void testGetInjectableValueId() {
        Assert.assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[creator property, name 'null'; inject id 'null']", creatorProperty.toString());
    }
}