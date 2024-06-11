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
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
        assertEquals(Salvar.SALVAR, salvar.SALVAR);
    }
    
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        
        File file = new File("example.jaw.xml");
        assertEquals(true, filtro.accept(file));
        
        String description = "XML JAW files";
        assertEquals(description, filtro.getDescription());
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        
        File file = new File("example.jaw");
        assertEquals(true, filtro.accept(file));
        
        String description = "JAW files";
        assertEquals(description, filtro.getDescription());
    }
}