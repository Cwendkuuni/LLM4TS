import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import jaw.entrada.*;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 10);
    }

    @Test
    public void testSalvarConstructor() {
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarConstructorWithException() throws Exception {
        when(form.getEntidades()).thenThrow(new Exception());
        new Salvar(form, 10);
        verify(Main.janelaPrincipal, times(1)).log(anyString());
    }

    @Test
    public void testSalvarStripLeadingAndTrailingQuotes() throws IOException {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new Vector<Entidade>());
        Salvar salvar = new Salvar(form, 10);
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals(anyString(), Util.stripLeadingAndTrailingQuotes("foo bar"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo and bar\"", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testSalvarStripLeadingHyphens() throws IOException {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new Vector<Entidade>());
        Salvar salvar = new Salvar(form, 10);
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(true);
        assertTrue(filtro.accept(file));
        when(file.getName()).thenReturn("test.jaw.xml");
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(true);
        assertTrue(filtro.accept(file));
        when(file.getName()).thenReturn("test.jaw");
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

}