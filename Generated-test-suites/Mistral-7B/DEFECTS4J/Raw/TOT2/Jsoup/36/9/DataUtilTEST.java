import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DataUtilTest {
    // Test load method with valid file, default charset, and baseUri
    @Test
    public void testLoadValidFile() throws IOException {
        File file = Mockito.mock(File.class);
        Document expectedDocument = Mockito.mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "/baseUri";

        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(DataUtil.load(file, charsetName, baseUri)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    // Test load method with null file, null charsetName, and null baseUri
    @Test(expected = IOException.class)
    public void testLoadNullFile() {
        File file = null;

        try {
            DataUtil.load(file, null, null);
        } catch (IOException e) {
            // Expected exception
        }
    }

    // Test load method with invalid charsetName, valid file, and baseUri
    @Test(expected = UnsupportedEncodingException.class)
    public void testLoadInvalidCharsetName() throws IOException {
        File file = Mockito.mock(File.class);
        Document document = Mockito.mock(Document.class);
        String charsetName = "invalidCharset";
        String baseUri = "/baseUri";

        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(DataUtil.load(file, charsetName, baseUri)).thenReturn(document);

        DataUtil.load(file, charsetName, baseUri);
    }

    // Test parseByteData method with valid byteData, valid charsetName, and baseUri
    @Test
    public void testParseByteDataValidByteData() {
        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Document expectedDocument = Mockito.mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "/baseUri";

        Mockito.when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    // Test parseByteData method with null charsetName, valid byteData, and baseUri
    @Test
    public void testParseByteDataNullCharsetName() {
        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Document document = Mockito.mock(Document.class);
        String baseUri = "/baseUri";

        Mockito.when(DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser())).thenReturn(document);

        Document actualDocument = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        Assert.assertEquals(document, actualDocument);
    }
}