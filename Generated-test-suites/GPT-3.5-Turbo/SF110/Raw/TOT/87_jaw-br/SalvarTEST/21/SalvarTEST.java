import jaw.entrada.*;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }
    
    // Test case for the constructor with exception
    @Test(expected = NullPointerException.class)
    public void testConstructor_Exception() {
        ProcessarEntidades form = null;
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
    }    
    
    // Test case for the accept method of the Filtro2 class
    @Test
    public void testFiltro2_Accept() {
        Filtro2 filtro2 = new Filtro2();
        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.txt")));
    }
    
    // Test case for the getDescription method of the Filtro2 class
    @Test
    public void testFiltro2_GetDescription() {
        Filtro2 filtro2 = new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    // Test case for the accept method of the Filtro class
    @Test
    public void testFiltro_Accept() {
        Filtro filtro = new Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.txt")));
    }
    
    // Test case for the getDescription method of the Filtro class
    @Test
    public void testFiltro_GetDescription() {
        Filtro filtro = new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
    
    // Test case for the salvar method with tipoSalvar = 10
    @Test
    public void testSalvar_TipoSalvar10() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
    }
    
    // Test case for the salvar method with tipoSalvar = 20
    @Test
    public void testSalvar_TipoSalvar20() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
    }
    
    // Test case for the salvar method with tipoSalvar = 30
    @Test
    public void testSalvar_TipoSalvar30() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 30);
    }
    
    // Test case for the salvar method with an exception
    @Test(expected = NullPointerException.class)
    public void testSalvar_Exception() {
        ProcessarEntidades form = null;
        Salvar salvar = new Salvar(form, 10);
    }

}