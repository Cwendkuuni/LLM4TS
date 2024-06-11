import org.junit.Test;
import org.apache.commons.cli.ParseException;
import java.io.File;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            TypeHandler.createValue("test", String.class);
            TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
            TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
            TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
            TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
            TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
            TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
            TypeHandler.createValue("test.txt, test2.txt", PatternOptionBuilder.FILES_VALUE);
            TypeHandler.createValue("http://www.google.com", PatternOptionBuilder.URL_VALUE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObject() {
        try {
            TypeHandler.createObject("java.lang.String");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            TypeHandler.createNumber("10");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateClass() {
        try {
            TypeHandler.createClass("java.lang.String");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDate() {
        TypeHandler.createDate("2020-01-01");
    }

    @Test
    public void testCreateURL() {
        try {
            TypeHandler.createURL("http://www.google.com");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile() {
        TypeHandler.createFile("test.txt");
    }

    @Test
    public void testCreateFiles() {
        TypeHandler.createFiles("test.txt, test2.txt");
    }
}