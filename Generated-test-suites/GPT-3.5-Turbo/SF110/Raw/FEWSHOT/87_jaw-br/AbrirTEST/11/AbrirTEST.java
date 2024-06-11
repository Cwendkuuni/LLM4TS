import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testConstructorWithStringParamEndingWithXml() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "file.xml";
        Abrir abrir = new Abrir(form, arquivo);
        // add assertions
    }
    
    @Test
    public void testConstructorWithStringParamNotEndingWithXml() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "file.xyz";
        Abrir abrir = new Abrir(form, arquivo);
        // add assertions
    }
    
    @Test
    public void testConstructorWithIntParamABRIR_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = Abrir.ABRIR_XML;
        Abrir abrir = new Abrir(form, tipoAbrir);
        // add assertions
    }
    
    @Test
    public void testConstructorWithIntParamABRIR() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = Abrir.ABRIR;
        Abrir abrir = new Abrir(form, tipoAbrir);
        // add assertions
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "file.jaw");
        abrir.abrirSerializado("file.jaw");
        // add assertions
    }
    
    @Test
    public void testAbrirSerializadoWrongExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "file.xyz");
        abrir.abrirSerializado("file.xyz");
        // add assertions
    }
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "file.jaw.xml");
        abrir.abrirXML("file.jaw.xml");
        // add assertions
    }
    
    @Test
    public void testAbrirXMLWrongExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "file.xml");
        abrir.abrirXML("file.xml");
        // add assertions
    }

}