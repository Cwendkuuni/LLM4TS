package jaw.entrada;

import org.junit.Test;
import static org.junit.Assert.*;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {
    
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testConstructorException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        String str1 = "\"foo\"";
        String str2 = "foo \"bar\"";
        String str3 = "\"foo\" bar";
        String str4 = "\"foo\" and \"bar\"";
        String str5 = "";
        
        assertEquals("foo", salvar.stripLeadingAndTrailingQuotes(str1));
        assertEquals("foo \"bar\"", salvar.stripLeadingAndTrailingQuotes(str2));
        assertEquals("\"foo\" bar", salvar.stripLeadingAndTrailingQuotes(str3));
        assertEquals("\"foo\" and \"bar\"", salvar.stripLeadingAndTrailingQuotes(str4));
        assertEquals("", salvar.stripLeadingAndTrailingQuotes(str5));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        String str1 = "-f";
        String str2 = "--foo";
        String str3 = "---foo";
        String str4 = "";
        
        assertEquals("f", salvar.stripLeadingHyphens(str1));
        assertEquals("foo", salvar.stripLeadingHyphens(str2));
        assertEquals("-foo", salvar.stripLeadingHyphens(str3));
        assertNull(salvar.stripLeadingHyphens(str4));
    }
    
    @Test
    public void testFiltro2Accept() {
        Salvar salvar = new Salvar();
        File file = new File("test.jaw.xml");
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        
        assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltroAccept() {
        Salvar salvar = new Salvar();
        File file = new File("test.jaw");
        Salvar.Filtro filtro = salvar.new Filtro();
        
        assertTrue(filtro.accept(file));
    }
}