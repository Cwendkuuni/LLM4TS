import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import jaw.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.struct.*;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = Salvar.SALVAR_XML;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }
    
    @Test
    public void testSalvarFiltro2Accept() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
        assertTrue(filtro.accept(new File("folder/test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertFalse(filtro.accept(new File("test.jaw")));
    }
    
    @Test
    public void testSalvarFiltro2GetDescription() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
    @Test
    public void testSalvarFiltroAccept() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
        assertTrue(filtro.accept(new File("folder/test.jaw")));
        assertFalse(filtro.accept(new File("test.xml")));
        assertFalse(filtro.accept(new File("test.jaw.xml")));
    }
    
    @Test
    public void testSalvarFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}