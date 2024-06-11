import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {
  
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        String arquivo = salvar.arquivo;
        assertEquals("", arquivo);
    }
  
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        File file = new File("test.jaw.xml");
        boolean acceptResult = filtro2.accept(file);
        assertTrue(acceptResult);
    }
  
    @Test
    public void testGetDescriptionFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();
        String description = filtro2.getDescription();
        assertEquals("XML JAW files", description);
    }
  
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("test.jaw");
        boolean acceptResult = filtro.accept(file);
        assertTrue(acceptResult);
    }
  
    @Test
    public void testGetDescriptionFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        String description = filtro.getDescription();
        assertEquals("JAW files", description);
    }
}