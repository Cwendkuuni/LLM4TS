import static org.junit.Assert.*;
import org.junit.Test;
import jaw.gui.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class AbrirTest {
	
	@Test
	public void testConstructorWithArquivoJawXML() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.jaw.xml");
		assertEquals("", abrir.xml);
		assertSame(form, abrir.form);
	}
	
	@Test
	public void testConstructorWithArquivoJaw() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.jaw");
		assertEquals("", abrir.xml);
		assertSame(form, abrir.form);
		assertTrue(form.getNomeArquivoSalvo().endsWith("test.jaw"));
	}
	
	@Test
	public void testConstructorWithTipoAbrir10() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, 10);
		assertEquals("", abrir.xml);
		assertSame(form, abrir.form);
	}
	
	@Test
	public void testConstructorWithTipoAbrir20() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, 20);
		assertEquals("", abrir.xml);
		assertSame(form, abrir.form);
		assertTrue(form.getNomeArquivoSalvo().endsWith("test.jaw"));
	}
	
	@Test
	public void testAbrirXMLWithJawXMLFile() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.jaw.xml");
		abrir.abrirXML(abrir.form.getNomeArquivoSalvo());
		assertNotNull(form.getEntidades());
	}
	
	@Test
	public void testAbrirXMLWithNonJawXMLFile() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.xml");
		abrir.abrirXML(abrir.form.getNomeArquivoSalvo());
		assertNull(form.getEntidades());
	}
	
	@Test
	public void testAbrirSerializadoWithJawFile() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.jaw");
		abrir.abrirSerializado(abrir.form.getNomeArquivoSalvo());
		assertNotNull(form.getEntidades());
	}
	
	@Test
	public void testAbrirSerializadoWithNonJawFile() {
		ProcessarEntidades form = new ProcessarEntidades();
		Abrir abrir = new Abrir(form, "test.txt");
		abrir.abrirSerializado(abrir.form.getNomeArquivoSalvo());
		assertNull(form.getEntidades());
	}
	
    @Test
    public void testFiltro2AcceptWithJawXMLFile() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        File file = new File("test.jaw.xml");
        assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2AcceptWithNonJawXMLFile() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        File file = new File("test.xml");
        assertFalse(filtro2.accept(file));
    }
    
    @Test
    public void testFiltroDescription2() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAcceptWithJawFile() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        File file = new File("test.jaw");
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroAcceptWithNonJawFile() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        File file = new File("test.txt");
        assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltroDescription() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
	
}