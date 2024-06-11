import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn_StringTypical() {
        Joiner joiner = Joiner.on(", ");
        // Typical use case
        String result = joiner.join("John", "Doe");
        assertEquals("John, Doe", result);
    }

    @Test
    public void testOn_StringEmpty() {
        Joiner joiner = Joiner.on(", ");
        // Edge case: empty String
        String result = joiner.join("", "");
        assertEquals(", ", result);
    }

    @Test
    public void testOn_CharTypical() {
        Joiner joiner = Joiner.on(',');
        // Typical use case
        String result = joiner.join("John", "Doe");
        assertEquals("John,Doe", result);
    }

    @Test
    public void testOn_CharEmpty() {
        Joiner joiner = Joiner.on(',');
        // Edge case: empty String
        String result = joiner.join("", "");
        assertEquals(",", result);
    }

    @Test
    public void testAppendTo_IterableTypical() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add("Doe");
        // Typical use case
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("John, Doe", builder.toString());
    }

    @Test
    public void testAppendTo_IterableEmpty() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        // Edge case: empty list
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_IteratorTypical() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add("Doe");
        Iterator<String> iterator = list.iterator();
        // Typical use case
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, iterator);
        assertEquals("John, Doe", builder.toString());
    }

    @Test
    public void testAppendTo_IteratorEmpty() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        // Edge case: empty iterator
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, iterator);
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_ArrayTypical() {
        Joiner joiner = Joiner.on(", ");
        String[] array = new String[]{"John", "Doe"};
        // Typical use case
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("John, Doe", builder.toString());
    }

    @Test
    public void testAppendTo_ArrayEmpty() {
        Joiner joiner = Joiner.on(", ");
        String[] array = new String[]{};
        // Edge case: empty array
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_ObjectVariadicTypical() {
        Joiner joiner = Joiner.on(", ");
        // Typical use case
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "John", "Doe");
        assertEquals("John, Doe", builder.toString());
    }

    @Test
    public void testAppendTo_ObjectVariadicEmpty() {
        Joiner joiner = Joiner.on(", ");
        // Edge case: no objects provided
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder);
        assertEquals("", builder.toString());
    }

    @Test
    public void testJoin_IterableTypical() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add("Doe");
        // Typical use case
        String result = joiner.join(list);
        assertEquals("John, Doe", result);
    }

    @Test
    public void testJoin_IterableEmpty() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        // Edge case: empty list
        String result = joiner.join(list);
        assertEquals("", result);
    }

    @Test
    public void testJoin_IteratorTypical() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add("Doe");
        Iterator<String> iterator = list.iterator();
        // Typical use case
        String result = joiner.join(iterator);
        assertEquals("John, Doe", result);
    }

    @Test
    public void testJoin_IteratorEmpty() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        // Edge case: empty iterator
        String result = joiner.join(iterator);
        assertEquals("", result);
    }

    @Test
    public void testJoin_ArrayTypical() {
        Joiner joiner = Joiner.on(", ");
        String[] array = new String[]{"John", "Doe"};
        // Typical use case
        String result = joiner.join(array);
        assertEquals("John, Doe", result);
    }

    @Test
    public void testJoin_ArrayEmpty() {
        Joiner joiner = Joiner.on(", ");
        String[] array = new String[]{};
        // Edge case: empty array
        String result = joiner.join(array);
        assertEquals("", result);
    }

    @Test
    public void testJoin_ObjectVariadicTypical() {
        Joiner joiner = Joiner.on(", ");
        // Typical use case
        String result = joiner.join("John", "Doe");
        assertEquals("John, Doe", result);
    }

    @Test
    public void testJoin_ObjectVariadicEmpty() {
        Joiner joiner = Joiner.on(", ");
        // Edge case: no objects provided
        String result = joiner.join();
        assertEquals("", result);
    }

    @Test
    public void testUseForNull_Typical() {
        Joiner joiner = Joiner.on(", ").useForNull("NULL");
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add(null);
        list.add("Doe");
        // Typical use case
        String result = joiner.join(list);
        assertEquals("John, NULL, Doe", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_Exception() {
        Joiner joiner = Joiner.on(", ").useForNull("NULL");
        // Error scenario: cannot call useForNull twice
        joiner.useForNull("NULL");
    }

    @Test
    public void testSkipNulls_IteratorTypical() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        List<String> list = new ArrayList<>();
        list.add("John");
        list.add(null);
        list.add("Doe");
        Iterator<String> iterator = list.iterator();
        // Typical use case
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, iterator);
        assertEquals("John, Doe", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_Exception1() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        // Error scenario: cannot call useForNull after skipNulls
        joiner.useForNull("NULL");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_Exception2() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        // Error scenario: cannot use .skipNulls with maps
        joiner.withKeyValueSeparator(":").skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator_Typical() {
        Joiner joiner = Joiner.on(", ").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", "30");
        // Typical use case
        String result = joiner.join(map);
        assertEquals("name=John, age=30", result);
    }

    // Other test methods for MapJoiner can be similarly added

}