
package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.ProcessarEntidades;
import jaw.Serializacao;
import jaw.SerializacaoXML;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import javax.swing.JOptionPane;

public class AbrirTest {

	@Test
	public void testAbrir() {
		ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
		Abrir abrir = new Abrir(form, "test.jaw.xml");
		assertEquals("", abrir.xml);
		assertNotNull(abrir.form);
		Mockito.verify(form).setNomeArquivoSalvo("test.jaw.xml");
		Mockito.verify(form).setEntidades(Mockito.any());
	}

	@Test
	public void testAbrirXML() {
		ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
		Abrir abrir = new Abrir(form, "test.jaw.xml");
		assertEquals("", abrir.xml);
		assertNotNull(abrir.form);
		Mockito.verify(form).setNomeArquivoSalvo("test.jaw.xml");
		Mockito.verify(form).setEntidades(Mockito.any());
	}

	@Test
	public void testAbrirSerializado() {
		ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
		Abrir abrir = new Abrir(form, "test.jaw");
		assertEquals("", abrir.xml);
		assertNotNull(abrir.form);
		Mockito.verify(form).setNomeArquivoSalvo("test.jaw");
		Mockito.verify(form).setEntidades(Mockito.any());
	}

	@Test
	public void testFiltro2Accept() {
		Abrir.Filtro2 filtro = new Abrir.Filtro2();
		File file = Mockito.mock(File.class);
		Mockito.when(file.isDirectory()).thenReturn(true);
		assertTrue(filtro.accept(file));
		Mockito.when(file.isDirectory()).thenReturn(false);
		Mockito.when(file.getName()).thenReturn("test.jaw.xml");
		assertTrue(filtro.accept(file));
		Mockito.when(file.getName()).thenReturn("test.txt");
		assertFalse(filtro.accept(file));
	}

	@Test
	public void testFiltro2GetDescription() {
		Abrir.Filtro2 filtro = new Abrir.Filtro2();
		assertEquals("XML JAW files", filtro.getDescription());
	}

	@Test
	public void testFiltroAccept() {
		Abrir.Filtro filtro = new Abrir.Filtro();
		File file = Mockito.mock(File.class);
		Mockito.when(file.isDirectory()).thenReturn(true);
		assertTrue(filtro.accept(file));
		Mockito.when(file.isDirectory()).thenReturn(false);
		Mockito.when(file.getName()).thenReturn("test.jaw");
		assertTrue(filtro.accept(file));
		Mockito.when(file.getName()).thenReturn("test.txt");
		assertFalse(filtro.accept(file));
	}

	@Test
	public void testFiltroGetDescription() {
		Abrir.Filtro filtro = new Abrir.Filtro();
		assertEquals("JAW files", filtro.getDescription());
	}
}
