package jaw.entrada;

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
    public void testAbrir() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "example.jaw.xml";

        Abrir abrir = new Abrir(form, arquivo);

        assertNull(abrir.xml);
        assertSame(form, abrir.form);
        assertEquals(10, Abrir.ABRIR_XML);
        assertEquals(20, Abrir.ABRIR);

        // Add more assertions as needed
    }

    @Test
    public void testAbrir2() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;

        Abrir abrir = new Abrir(form, tipoAbrir);
        
        assertNull(abrir.xml);
        assertSame(form, abrir.form);
        assertEquals(10, Abrir.ABRIR_XML);
        assertEquals(20, Abrir.ABRIR);

        // Add more assertions as needed
    }

    // Add more test methods as needed

}