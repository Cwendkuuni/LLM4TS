
package com.fasterxml.jackson.databind.node;

import org.junit.*;
import static org.junit.Assert.*;

public class POJONodeTest {

    @BeforeClass
    public static void setUpClass() {
        // Class initialization
    }

    @Before
    public void setUp() {
        // Instance initialization
    }

    @After
    public void tearDown() {
        // Cleanup
    }

    @AfterClass
    public static void tearDownClass() {
        // Class cleanup
    }

    @Test
    public void testgetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testSerialize() throws Exception {
        POJONode node = new POJONode(new Object());
        String json = mapper.writeValueAsString(node);
        assertNotNull(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        node.serialize(gen, new SerializerProvider());
        assertSame(json, gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        Object pojo = node.getPojo();
        assertNotNull(pojo);
        assertEquals(pojo.getClass(), new Object().getClass());
    }

    @Test(expected = IOException.class)
    public void testSerializeWithIOException() throws IOException {
        POJONode node = new POJONode(new Object());
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream());
        node.serialize(gen, new SerializerProvider());
        fail("IOException should be thrown");
    }

    @Test(expected = NullPointerException.class)
    public void testGetPojoWithNull() {
        POJONode node = new POJONode(null);
        assertNull(node.getPojo());
    }
}