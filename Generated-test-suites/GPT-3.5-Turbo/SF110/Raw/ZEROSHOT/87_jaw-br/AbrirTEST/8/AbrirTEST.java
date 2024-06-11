import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AbrirTest {

    @Test
    public void testConstructor1() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "filename.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        // Add assertions
    }
    
    @Test
    public void testConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        // Add assertions
    }
    
    @Test
    public void testabrirSerializado() {
        Abrir abrir = new Abrir(new ProcessarEntidades(), "filename.jaw");
        abrir.abrirSerializado(arquivo);
        // Add assertions
    }
    
    @Test
    public void testabrirXML() {
        Abrir abrir = new Abrir(new ProcessarEntidades(), "filename.jaw.xml");
        abrir.abrirXML(arquivo);
        // Add assertions
    }
    
}