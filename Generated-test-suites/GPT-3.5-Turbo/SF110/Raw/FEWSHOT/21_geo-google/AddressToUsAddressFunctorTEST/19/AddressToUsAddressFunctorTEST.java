package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        
        // Test case when country is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCountry());
        
        // Test case when country is not null
        AddressDetails.Country country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        
        // Test case when countryNames is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCountry());
        
        // Test case when countryNames is not empty
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        country.setCountryNameCode(countryNames);
        
        // Test case when state is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getState());
        
        // Test case when state is not null
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        
        // Test case when names is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getState());
        
        // Test case when names is not empty
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        adminArea.setAdministrativeAreaName(names);
        
        // Test case when subAdminArea is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCounty());
        
        // Test case when subAdminArea is not null
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        // Test case when subNames is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCounty());
        
        // Test case when subNames is not empty
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        subAdminArea.setSubAdministrativeAreaName(subNames);
        
        // Test case when locality is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCity());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getPostalCode());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine1());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine2());
        
        // Test case when locality is not null
        Locality locality = new Locality();
        adminArea.setLocality(locality);
        
        // Test case when localNames is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCity());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getPostalCode());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine1());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine2());
        
        // Test case when localNames is not empty
        List<Locality.LocalityName> localNames = new ArrayList<>();
        locality.setLocalityName(localNames);
        
        // Test case when postalCode is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getPostalCode());
        
        // Test case when postalCode is not null
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        // Test case when numbers is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getPostalCode());
        
        // Test case when numbers is not empty
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        postalCode.setPostalCodeNumber(numbers);
        
        // Test case when thoroughfare is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine1());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine2());
        
        // Test case when thoroughfare is not null
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        // Test case when thoroughfares is empty
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine1());
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAddressLine2());
        
        // Test case when thoroughfares is not empty
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfare.setThoroughfareName(thoroughfares);
        
        // Test case when coordinate is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getCoordinate());
        
        // Test case when coordinate is not null
        addr.setCoordinate(new GeoCoordinate());
        assertEquals(addr.getCoordinate(), AddressToUsAddressFunctor.getInstance().execute(addr).getCoordinate());
        
        // Test case when accuracy is null
        assertNull(AddressToUsAddressFunctor.getInstance().execute(addr).getAccuracy());
        
        // Test case when accuracy is not null
        addr.setAccuracy(0.5);
        assertEquals(Double.valueOf(0.5), AddressToUsAddressFunctor.getInstance().execute(addr).getAccuracy());
    }
}