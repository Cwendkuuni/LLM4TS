
import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumSerializer serializer = new EnumSerializer();

        // Test case 1: Verify the serialization of an enum using index
        Enum<?> en1 = MyEnum.FIRST;
        String expected1 = "0";
        String actual1 = serializer.serialize(en1, null, null);
        Assert.assertEquals(expected1, actual1);

        // Test case 2: Verify the serialization of an enum using toString()
        Enum<?> en2 = MyEnum.SECOND;
        String expected2 = "SECOND";
        String actual2 = serializer.serialize(en2, null, null);
        Assert.assertEquals(expected2, actual2);

        // Test case 3: Verify the serialization of an enum using explicit overrides
        Enum<?> en3 = MyEnum.THIRD;
        String expected3 = "THIRD";
        String actual3 = serializer.serialize(en3, null, null);
        Assert.assertEquals(expected3, actual3);
    }

    @Test
    public void testGetSchema() {
        EnumSerializer serializer = new EnumSerializer();

        // Test case 1: Verify the schema for an enum serialized as index
        Class<?> enumClass1 = MyEnum.class;
        String expected1 = "{\"type\":\"integer\",\"enum\":[0,1,2]}";
        String actual1 = serializer.getSchema(enumClass1, null, null);
        Assert.assertEquals(expected1, actual1);

        // Test case 2: Verify the schema for an enum serialized as string
        Class<?> enumClass2 = MyEnumString.class;
        String expected2 = "{\"type\":\"string\",\"enum\":[\"FIRST\",\"SECOND\",\"THIRD\"]}";
        String actual2 = serializer.getSchema(enumClass2, null, null);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer serializer = new EnumSerializer();

        // Test case 1: Verify the format visitor for an enum serialized as index
        JsonFormatVisitorWrapper visitor1 = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor1, null);
        String expected1 = "visitIntFormat";
        String actual1 = visitor1.getResult();
        Assert.assertEquals(expected1, actual1);

        // Test case 2: Verify the format visitor for an enum serialized as string
        JsonFormatVisitorWrapper visitor2 = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor2, null);
        String expected2 = "visitStringFormat";
        String actual2 = visitor2.getResult();
        Assert.assertEquals(expected2, actual2);
    }
}
