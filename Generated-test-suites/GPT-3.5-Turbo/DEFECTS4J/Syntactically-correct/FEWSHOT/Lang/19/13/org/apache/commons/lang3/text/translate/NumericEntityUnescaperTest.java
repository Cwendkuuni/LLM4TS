
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslateWithNumericEntities() {
        StringBuilder sb = new StringBuilder();

        // test single decimal entity
        sb.append("&#65;");
        assertEquals("A", translator.translate(sb));
        sb.setLength(0);

        // test single hex entity
        sb.append("&#x41;");
        assertEquals("A", translator.translate(sb));
        sb.setLength(0);

        // test multiple entities
        sb.append("&#65;&#x42;&#x43;");
        assertEquals("ABC", translator.translate(sb));
        sb.setLength(0);

        // test entities with leading zeros
        sb.append("&#065;&#x0042;&#x0043;");
        assertEquals("ABC", translator.translate(sb));
        sb.setLength(0);

        // test entity without semicolon
        sb.append("&#65");
        assertEquals("A", translator.translate(sb));
        sb.setLength(0);

        // test entity with invalid hex character
        sb.append("&#x4G;");
        assertEquals("&#x4G;", translator.translate(sb));
        sb.setLength(0);

        // test entity with invalid decimal character
        sb.append("&#-65;");
        assertEquals("&#-65;", translator.translate(sb));
        sb.setLength(0);
    }

    @Test
    public void testTranslateWithNonNumericEntities() {
        StringBuilder sb = new StringBuilder();

        // test entity with alphabetic characters
        sb.append("&#foo;");
        assertEquals("&#foo;", translator.translate(sb));
        sb.setLength(0);

        // test entity without digits
        sb.append("&#;");
        assertEquals("&#;", translator.translate(sb));
        sb.setLength(0);

        // test entity without ampersand
        sb.append("#65;");
        assertEquals("#65;", translator.translate(sb));
        sb.setLength(0);

        // test empty input
        sb.append("");
        assertEquals("", translator.translate(sb));
        sb.setLength(0);
    }
}
