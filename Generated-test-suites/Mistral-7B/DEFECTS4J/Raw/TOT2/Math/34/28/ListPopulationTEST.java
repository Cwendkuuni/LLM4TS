import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

public class ListPopulationTest1 {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);

        listPopulation.setChromosomes(chromosomes);

        Assert.assertEquals(5, listPopulation.getPopulationLimit());
        Assert.assertEquals(5, listPopulation.getPopulationSize());
        Assert.assertSame(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testListPopulationWithInvalidPopulationLimit() {
        ListPopulation listPopulation = null;
        try {
            listPopulation = new ListPopulation(-1);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is(StringMatchers.containsString("Population limit must be a positive number")));
        }
        Assert.assertNull(listPopulation);
    }

    @Test
    public void testListPopulationWithNullChromosomes() {
        ListPopulation listPopulation = null;
        try {
            listPopulation = new ListPopulation(new ArrayList<>());
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is(StringMatchers.containsString("List of chromosomes can't be null")));
        }
        Assert.assertNull(listPopulation);
    }

    @Test
    public void testListPopulationWithChromosomesBiggerThanPopulationLimit() {
        ListPopulation listPopulation = null;
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        for (int i = 0; i < 10; i++) {
            chromosomes.add(chromosome);
        }

        try {
            listPopulation = new ListPopulation(5, chromosomes);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is(StringMatchers.containsString("List of chromosomes is bigger than population limit")));
        }
        Assert.assertNull(listPopulation);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(5);

        Chromosome chromosome = new DummyChromosome(1);

        listPopulation.addChromosome(chromosome);

        Assert.assertEquals(6, listPopulation.getPopulationSize());
        Assert.assertSame(chromosome, listPopulation.getChromosomes().get(5));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(5);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        for (int i = 0; i < 11; i++) {
            chromosomes.add(chromosome);
        }

        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(5);

        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(5);
        Chromosome chromosome3 = new DummyChromosome(3);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        Chromosome fittestChromosome = listPopulation.getFittestChromosome();

        Assert.assertSame(chromosome2, fittestChromosome);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation listPopulation = new ListPopulation(5);

        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(5);
        Chromosome chromosome3 = new DummyChromosome(3);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        String expected = "[" + chromosome1.toString() + ", " + chromosome2.toString() + ", " + chromosome3.toString() + "]";

        Assert.assertEquals(expected, listPopulation.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation listPopulation = new ListPopulation(5);

        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(5);
        Chromosome chromosome3 = new DummyChromosome(3);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        Iterator<Chromosome> iterator = listPopulation.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(chromosome1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(chromosome2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(chromosome3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    private static class DummyChromosome implements Chromosome {
        private final int fitness;

        public DummyChromosome(int fitness) {
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Chromosome o) {
            return this.fitness > o.getFitnessValue() ? 1 : -1;
        }

        @Override
        public int getFitnessValue() {
            return this.fitness;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + "fitness=" + fitness + '}';
        }
    }
}