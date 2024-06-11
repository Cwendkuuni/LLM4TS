package jaw.entrada;

import jaw.gui.ProcessarEntidades;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {

    @Test
    public void testAbrir() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "path/to/arquivo.jaw.xml";
        Abrir instance = new Abrir(form, arquivo);
        // Perform test assertions
        // ...
    }

    @Test
    public void testAbrir2() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = Abrir.ABRIR_XML;
        Abrir instance = new Abrir(form, tipoAbrir);
        // Perform test assertions
        // ...
    }

    @Test
    public void testAbrirSerializado() {
        String arquivo = "path/to/arquivo.jaw";
        Abrir instance = new Abrir(null, null);
        instance.abrirSerializado(arquivo);
        // Perform test assertions
        // ...
    }

    @Test
    public void testAbrirXML() {
        String arquivo = "path/to/arquivo.jaw.xml";
        Abrir instance = new Abrir(null, null);
        instance.abrirXML(arquivo);
        // Perform test assertions
        // ...
    }
}