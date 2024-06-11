import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 1), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
}