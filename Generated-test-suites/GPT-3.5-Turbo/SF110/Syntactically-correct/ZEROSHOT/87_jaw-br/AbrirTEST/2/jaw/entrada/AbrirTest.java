import jaw.entrada.*;
import jaw.gui.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbrirTest {

    @Test
    public void testAbrirConstructorArquivoJawXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "teste.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        // Perform assertions
        // ...
    }

    @Test
    public void testAbrirConstructorArquivoJaw() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "teste.jaw";
        Abrir abrir = new Abrir(form, arquivo);
        // Perform assertions
        // ...
    }

    @Test
    public void testAbrirConstructorTipoAbrir10() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        // Perform assertions
        // ...
    }

    @Test
    public void testAbrirConstructorTipoAbrir20() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 20;
        Abrir abrir = new Abrir(form, tipoAbrir);
        // Perform assertions
        // ...
    }

    @Test
    public void testAbrirSerializado() {
        Abrir abrir = new Abrir();
        String arquivo = "teste.jaw";
        abrir.abrirSerializado(arquivo);
        // Perform assertions
        // ...
    }

    @Test
    public void testAbrirXML() {
        Abrir abrir = new Abrir();
        String arquivo = "teste.jaw.xml";
        abrir.abrirXML(arquivo);
        // Perform assertions
        // ...
    }

    @Test
    public void testFiltro2Accept() {
        Abrir abrir = new Abrir();
        File file = new File("teste.jaw.xml");
        boolean result = abrir.new Filtro2().accept(file);
        // Perform assertions
        // ...
    }

    @Test
    public void testFiltro2GetDescription() {
        Abrir abrir = new Abrir();
        String description = abrir.new Filtro2().getDescription();
        // Perform assertions
        // ...
    }

    @Test
    public void testFiltroAccept() {
        Abrir abrir = new Abrir();
        File file = new File("teste.jaw");
        boolean result = abrir.new Filtro().accept(file);
        // Perform assertions
        // ...
    }

    @Test
    public void testFiltroGetDescription() {
        Abrir abrir = new Abrir();
        String description = abrir.new Filtro().getDescription();
        // Perform assertions
        // ...
    }

}