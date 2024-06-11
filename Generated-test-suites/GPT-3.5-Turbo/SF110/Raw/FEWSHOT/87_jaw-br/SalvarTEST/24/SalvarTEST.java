import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import org.junit.Test;

public class SalvarTest {
    
    // Testing the Salvar constructor with tipoSalvar = 10
    @Test
    public void testSalvarConstructorTipoSalvar10() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        
        // Asserting initial values
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }

    // Testing the Salvar constructor with tipoSalvar = 20
    @Test
    public void testSalvarConstructorTipoSalvar20() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        
        // Asserting initial values
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }
    
    // Testing the Salvar constructor with tipoSalvar = 30
    @Test
    public void testSalvarConstructorTipoSalvar30() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 30);
        
        // Asserting initial values
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }
    
    // Testing the Salvar constructor with exception
    @Test
    public void testSalvarConstructorException() {
        ProcessarEntidades form = new ProcessarEntidades();
        // Simulating an exception in form.getEntidades()
        form.setThrowException(true);
        Salvar salvar = new Salvar(form, 10);
        
        // In case of exception, no further code should be executed
        assertTrue(form.getLog().contains("Exception"));
        assertTrue(salvar.entidade.isEmpty());
    }

    // Testing the Salvar constructor with tipoSalvar != 10, 20, 30
    @Test
    public void testSalvarConstructorInvalidTipoSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 40);
        
        // Asserting initial values
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }
    
    // Testing the accept() method of Filtro2 class with .jaw.xml file
    @Test
    public void testFiltro2AcceptJAWXMLFile() {
        Salvar salvar = new Salvar();
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        
        File file = new File("sample.jaw.xml");
        assertTrue(filtro2.accept(file));
    }
    
    // Testing the accept() method of Filtro2 class with non .jaw.xml file
    @Test
    public void testFiltro2AcceptNonJAWXMLFile() {
        Salvar salvar = new Salvar();
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        
        File file = new File("sample.txt");
        assertFalse(filtro2.accept(file));
    }
    
    // Testing the getDescription() method of Filtro2 class
    @Test
    public void testFiltro2GetDescription() {
        Salvar salvar = new Salvar();
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    // Testing the accept() method of Filtro class with .jaw file
    @Test
    public void testFiltroAcceptJAWFile() {
        Salvar salvar = new Salvar();
        Salvar.Filtro filtro = salvar.new Filtro();
        
        File file = new File("sample.jaw");
        assertTrue(filtro.accept(file));
    }
    
    // Testing the accept() method of Filtro class with non .jaw file
    @Test
    public void testFiltroAcceptNonJAWFile() {
        Salvar salvar = new Salvar();
        Salvar.Filtro filtro = salvar.new Filtro();
        
        File file = new File("sample.txt");
        assertFalse(filtro.accept(file));
    }
    
    // Testing the getDescription() method of Filtro class
    @Test
    public void testFiltroGetDescription() {
        Salvar salvar = new Salvar();
        Salvar.Filtro filtro = salvar.new Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
}