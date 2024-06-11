import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class SalvarTest {
    
    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        Assert.assertNotNull(salvar);
    }
    
    @Test
    public void testSalvarComo() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        Assert.assertNotNull(salvar);
    }
    
    @Test
    public void testSalvarXml() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        Assert.assertNotNull(salvar);
    }
    
    @Test
    public void testFiltro2AcceptDirectory() {
        File file = new File("testDir");
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        Assert.assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2AcceptFile() {
        File file = new File("testFile.jaw.xml");
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        Assert.assertTrue(filtro2.accept(file));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        Assert.assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAcceptDirectory() {
        File file = new File("testDir");
        Salvar.Filtro filtro = new Salvar().new Filtro();
        Assert.assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroAcceptFile() {
        File file = new File("testFile.jaw");
        Salvar.Filtro filtro = new Salvar().new Filtro();
        Assert.assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        Assert.assertEquals("JAW files", filtro.getDescription());
    }
}