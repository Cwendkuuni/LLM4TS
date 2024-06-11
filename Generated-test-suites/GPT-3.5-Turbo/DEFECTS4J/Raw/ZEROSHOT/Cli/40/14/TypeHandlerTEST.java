import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        try {
            Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
            Assert.assertEquals("test", result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueStringClass() {
        try {
            String result = TypeHandler.createValue("test", String.class);
            Assert.assertEquals("test", result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueObjectType() {
        try {
            Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueNumberType() {
        try {
            Number result = TypeHandler.createValue("5.5", PatternOptionBuilder.NUMBER_VALUE);
            Assert.assertEquals(5.5, result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueDateType() {
        try {
            Date result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueClassType() {
        try {
            Class<?> result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
            Assert.assertEquals(String.class, result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueFileType() {
        try {
            File result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
            Assert.assertEquals("test.txt", result.getName());
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueExistingFileType() {
        try {
            FileInputStream result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueFilesType() {
        try {
            File[] result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValueUrlType() {
        try {
            URL result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
            Assert.assertEquals("http://www.example.com", result.toString());
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number result = TypeHandler.createNumber("5.5");
            Assert.assertEquals(5.5, result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            Assert.assertEquals(String.class, result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            Assert.assertEquals("http://www.example.com", result.toString());
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        Assert.assertEquals("test.txt", result.getName());
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream result = TypeHandler.openFile("test.txt");
            Assert.assertNotNull(result);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("file1.txt,file2.txt");
    }
}